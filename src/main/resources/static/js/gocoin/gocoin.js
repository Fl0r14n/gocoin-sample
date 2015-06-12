/* global angular */

'use strict';

angular.module('gocoin', ['ui.bootstrap', 'monospaced.qrcode']).config(['$compileProvider', function ($compileProvider) {
        return $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|bitcoin|litecoin|dogecoin):/);
    }]);

angular.module('gocoin').factory('GoCoinService', ['RestResource', function (restResource) {
        var self = this;
        self.imgPath = 'img/gocoin/';
        return {
            invoice: restResource.$rest('payment/gocoin'),
            exchange: restResource.$rest('payment/gocoin/exchange'),
            img: {
                btcButton: self.imgPath + 'btc_icon.png',
                ltcButton: self.imgPath + 'ltc_icon.png',
                xdgButton: self.imgPath + 'xdg_icon.png',
                noQRCode: self.imgPath + 'no_qr_code.png',
                expiredStamp: self.imgPath + 'expired_stamp.png',
                paid: self.imgPath + 'paid_stamp.png'
            }
        };
    }]);

angular.module('gocoin').directive('uiGoCoinButton', function () {
    return {
        replace: false,
        restrict: 'EA',
        scope: {
            type: '@',
            price: '@',
            currency: '@',
            orderId: '@',
            customerEmail: '@',
            customerName: '@',
            itemName: '@',
            itemSku: '@'
        },
        controllerAs: 'gocoin',
        controller: [
            '$scope',
            'GoCoinService',
            function ($scope, $gcservice) {
                var self = this;
                self.img = $gcservice.img;

                self.invoice = {
                    price_currency: $scope.type,
                    base_price: $scope.price,
                    base_price_currency: $scope.currency,
                    order_id: $scope.orderId,
                    customer_email: $scope.customerEmail,
                    customer_name: $scope.customerName,
                    item_name: $scope.itemName,
                    item_sku: $scope.itemSku
                };

                self.setUrl = function () {
                    var tabWindowId = window.open('about:blank', '_blank');
                    $gcservice.invoice.save(self.invoice, function (data) {
                        if (data) {
                            tabWindowId.location.href = data.gateway_url;
                        }
                    });
                };

                self.hasType = function () {
                    return !angular.isUndefined(self.invoice.price_currency);
                };

                self.isBitcoin = function () {
                    return self.invoice.price_currency.toUpperCase() === 'BTC';
                };

                self.isLitecoin = function () {
                    return self.invoice.price_currency.toUpperCase() === 'LTC';
                };

                self.isDogecoin = function () {
                    return self.invoice.price_currency.toUpperCase() === 'XDG';
                };
            }
        ],
        template: [
            '<a href="" target="_blank" ng-mousedown="gocoin.setUrl()" ng-model="gocoin.invoice" ng-if="gocoin.hasType()" >',
            '   <img ng-if="gocoin.isBitcoin()" ng-src="{{gocoin.img.btcButton}}" alt="Bitcoin" />',
            '   <img ng-if="gocoin.isLitecoin()" ng-src="{{gocoin.img.ltcButton}}" alt="Litecoin" />',
            '   <img ng-if="gocoin.isDogecoin()" ng-src="{{gocoin.img.xdgButton}}" alt="Dogecoin" />',
            '</a>'
        ].join('')
    };
});


angular.module('gocoin').directive('uiGoCoinPanel', function () {
    return {
        replace: false,
        restrict: 'EA',
        scope: {
            price: '@',
            currency: '@',
            orderId: '@',
            customerEmail: '@',
            customerName: '@',
            itemName: '@',
            itemSku: '@'
        },
        controllerAs: 'ctrl',
        controller: [
            '$scope',
            '$interval',
            '$modal',
            'GoCoinService',
            function ($scope, $interval, $modal, $gcservice) {
                var self = this;
                self.isCollapsed = true;
                self.type = undefined;
                self.time_remaining = null;
                self.img = $gcservice.img;

                self.openModal = function (size) {
                    $modal.open({
                        templateUrl: 'qrcodeModal.html',
                        size: size,
                        resolve: {
                            crypto_url: function () {
                                return self.invoice.crypto_url;
                            }
                        },
                        controller: function ($scope, $modalInstance, crypto_url) {
                            $scope.crypto_url = crypto_url;
                        }
                    });
                };

                self.showPayment = function (type) {
                    if (type !== self.type) {
                        self.type = type;
                        self.isCollapsed = false;
                        self.createNewInvoice({
                            base_price: $scope.price,
                            base_price_currency: $scope.currency,
                            price_currency: self.type,
                            order_id: $scope.orderId,
                            customer_email: $scope.customerEmail,
                            customer_name: $scope.customerName,
                            item_name: $scope.itemName,
                            item_sku: $scope.itemSku
                        });
                    } else {
                        self.isCollapsed = !self.isCollapsed;
                    }
                };

                self.createNewInvoice = function (invoice) {
                    $gcservice.invoice.save(invoice, function (data) {
                        if (data.id) {
                            self.invoice = data;
                            if (data.crypto_url) {
                                self.merchantName = unescape(data.crypto_url.substring(data.crypto_url.indexOf('label=') + 6));
                            }
                            self.timerInit();
                        }
                    });
                };

                self.timerInit = function () {
                    var etime, stime;
                    if (self.invoice.expires_at) {
                        etime = Math.round(Date.parse(self.invoice.expires_at));
                        stime = Math.round(Date.parse(self.invoice.server_time));
                        self.time_remaining = etime - stime;
                        if (self.time_remaining <= 0) {
                            self.time_remaining = 0;
                        }
                        self.startCountdown();
                        self.startStatusPoll();
                    }
                };

                self.timerExpired = function () {
                    self.stopCountdown();
                    self.stopStatusPoll();
                };

                self.checkStatus = function () {
                    $gcservice.invoice.get({id: self.invoice.id}, function (data) {
                        if (data.id) {
                            self.invoice = data;
                            if (self.invoice.status === 'underpaid') {
                                self.timerInit();
                            } else if (self.invoice.status === 'paid') {
                                self.timerExpired();
                            }
                        }
                    });
                };

                self.stopStatusPoll = function () {
                    if (self.statusTimer !== null) {
                        $interval.cancel(self.statusTimer);
                    }
                };

                self.startStatusPoll = function () {
                    self.stopStatusPoll();
                    return self.statusTimer = $interval(self.checkStatus, 30000);
                };

                self.stopCountdown = function () {
                    if (self.countdownTimer !== null) {
                        return $interval.cancel(self.countdownTimer);
                    }
                };

                self.startCountdown = function () {
                    self.stopCountdown();
                    return self.countdownTimer = $interval(self.countdown, 1000);
                };

                self.countdown = function () {
                    if (self.time_remaining > 0) {
                        return self.time_remaining -= 1000;
                    } else {
                        self.timerExpired();
                        self.checkStatus();
                    }
                };
            }],
        template: [
            '<div>',
            '    <button class="btn btn-default" ng-click="ctrl.showPayment(\'BTC\')">',
            '        <img ng-src="{{ctrl.img.btcButton}}" alt="Bitcoin" />',
            '    </button>',
            '    <button class="btn btn-default" ng-click="ctrl.showPayment(\'LTC\')">',
            '        <img ng-src="{{ctrl.img.ltcButton}}" alt="Litecoin" />',
            '    </button>',
            '    <button class="btn btn-default" ng-click="ctrl.showPayment(\'XDG\')">',
            '        <img ng-src="{{ctrl.img.xdgButton}}" alt="Litecoin" />',
            '    </button>',
            '    <div collapse="ctrl.isCollapsed">',
            '        <div class="well well-lg">',
            '            <style scoped>',
            '                .col-ms-4,',
            '                .col-ms-8 {',
            '                    position: relative;',
            '                    min-height: 1px;',
            '                    padding-left: 15px;',
            '                    padding-right: 15px; }',
            '                @media (min-width: 480px) {',
            '                    .col-ms-4,',
            '                    .col-ms-8 {',
            '                        float: left; }',
            '                    .col-ms-4 {',
            '                        width: 33.33333%; }',
            '                    .col-ms-8 {',
            '                        width: 66.66667%; }',
            '                }',
            '                .visible-ms {',
            '                    display: none !important;',
            '                }',
            '                @media (max-width: 480px) {',
            '                    .hidden-ms {',
            '                        display: none !important;',
            '                    }',
            '                    .visible-ms {',
            '                        display: block !important;',
            '                    }',
            '                }',
            '                .qrcode {',
            '                    max-width: 100%;',
            '                }',
            '                .payment-panel {',
            '                    max-width: 780px;',
            '                }',
            '                .payment-timer {',
            '                    font-size: 32px;',
            '                    font-style: italic;',
            '                    font-weight: bold;',
            '                    color: #a94442;',
            '                }',
            '                .payment-expired {',
            '                    position: absolute;',
            '                }',
            '                .modal-sm {',
            '                    margin: 30px auto !important;',
            '                    width: 222px !important;',
            '                }',
            '            </style>',
            '            <script type="text/ng-template" id="qrcodeModal.html">',
            '                <qrcode class="modal-payment" version="8" error-correction-level="M" size="220" data="{{crypto_url}}"></qrcode>',
            '            </script>',
            '            <div class="container-fluid payment-panel">',
            '                <div class="row center-block">',
            '                    <p class="h3">{{ctrl.merchantName}}</p>',
            '                </div>',
            '                <div class="row">',
            '                    <div class="col-ms-4" ng-show="ctrl.invoice.crypto_url">',
            '                        <qrcode class="hidden-ms" version="8" error-correction-level="M" size="220" data="{{ctrl.invoice.crypto_url}}"></qrcode>',
            '                        <a class="btn btn-default btn-block visible-ms" ng-click="ctrl.openModal(\'sm\')">',
            '                            Show QR code',
            '                        </a>',
            '                        <a class="btn btn-default btn-block" href="{{ctrl.invoice.crypto_url}}">Launch Wallet</a>',
            '                    </div>',
            '                    <div class="col-ms-4" ng-hide="ctrl.invoice.crypto_url">',
            '                        <img class="img-responsive" ng-src="{{ctrl.img.noQRCode}}" alt="no QR code available">',
            '                    </div>',
            '                    <div class="col-ms-8 ">',
            '                        <div ng-show="ctrl.time_remaining === 0 && ctrl.invoice.status === \'unpaid\'">',
            '                            <img class="payment-expired" ng-src="{{ctrl.img.expiredStamp}}" alt="expired"/>',
            '                        </div>',
            '                        <div ng-show="ctrl.invoice.status === \'paid\'">',
            '                            <img class="payment-expired" ng-src="{{ctrl.img.PaidStamp}}" alt="paid"/>',
            '                        </div>',
            '                        <p class="h4">Price: <b class="text-primary">{{ctrl.invoice.base_price}}</b><span class="text-primary"> {{ctrl.invoice.base_price_currency_detail.symbol}}</span></p>',
            '                        <p class="h4">Crypto: <span class="text-primary">{{ctrl.invoice.price}} {{ctrl.invoice.price_currency_detail.symbol}}</span></p>',
            '                        <b>{{ctrl.invoice.payment_address}}</b>',
            '                        <p class="text-center"><a href="" ng-click="ctrl.checkStatus()">Check payment status</a></p>',
            '                        <p class="text-center payment-timer" ng-show="ctrl.time_remaining > 0">{{ctrl.time_remaining | date:\'mm:ss\'}}*</p>',
            '                        <p class="h6 text-muted">*This address is only active during the allotted time.<br/>It is only useable for this purchase. Do not save it for any reason.</p>',
            '                    </div>',
            '                </div>',
            '            </div>',
            '        </div>',
            '    </div>',
            '</div>'
        ].join('')
    };
});
