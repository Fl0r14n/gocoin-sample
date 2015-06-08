'use strict';

angular.module('gocoin', ['ngResource', 'ui.bootstrap', 'monospaced.qrcode']).config(['$compileProvider', function ($compileProvider) {
        return $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|bitcoin|litecoin|dogecoin):/);
    }]);

angular.module('gocoin').factory('GoCoinService', ['$resource', function ($resource) {
        var self = this;
        self.api = 'api/v1/';
        self.endpoint = function (resource) {
            return self.api + resource + '/';
        };
        self.invoiceUrl = self.endpoint('payment/gocoin');
        self.exchangeUrl = self.endpoint('payment/gocoin/exchange');
        return {
            invoice: $resource(self.invoiceUrl + ':id/', {id: '@id'}, {
                'create': {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                },
                'list': {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            }),
            exchange: $resource(self.exchangeUrl, {}, {
                get: {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            })
        };
    }]);

angular.module('gocoin').controller('GoCoinController', ['$scope','$interval', '$modal', 'GoCoinService', function ($scope, $interval, $modal, $gcservice) {
        var self = this;
        self.isCollapsed = true;
        self.type = undefined;

        //dummy data remove this
        self.invoice = {
            base_price: "10.00",
            base_price_currency: "EUR",
            crypto_url: "bitcoin:1GacAiUccggYdG6QCeTeGxtEtkmQTQam36?amount=0.0515&label=Payment%20to%20acme+inc",
            merchant_id: "f2fe3ccb-e509-4d58-b652-ede24636c422",
            payment_address: "1GacAiUccggYdG6QCeTeGxtEtkmQTQam36",
            price: 0.0515,
            price_currency: "BTC",
            status: "unpaid",
            server_time: "2015-06-05T07:56:53.610Z",
            expires_at: "2015-06-05T08:11:53.572Z"
        };
        
        self.expiredOrPaid = function() {
            return self.invoice.status === 'expired' || self.invoice.status === 'paid';
        };
                
        self.showPayment = function (type) {
            if(type !== self.type) {
                self.type = type;
                self.isCollapsed = false;
                
                //to test
                self.invoice.price_currency = type;
                self.createNewInvoice(self.invoice);
            } else {
                self.isCollapsed = !self.isCollapsed;
            }
        };
        
        self.createNewInvoice = function(invoice) {
            //TODO get merchant's name
            console.log('HERE');
            $gcservice.invoice.create(invoice, function(data) {
                if(data) {
                    console.log('HERE1');
                    console.log(data);
                    self.invoice = data;
                    self.timerInit();
                }
            });
        };
        
        self.checkStatus = function() {
            //TODO
        };
        
        
        self.openModal = function(size) {
            $modal.open({
                templateUrl: 'qrcodeModal.html',
                size: size,
                resolve: {
                    crypto_url: function() {
                        return self.invoice.crypto_url;
                    }
                },
                controller: function($scope, $modalInstance, crypto_url) {
                    $scope.crypto_url = crypto_url;
                }
            });
        };
                
        self.startCountdown = function () {
            return self.countdown = $interval(self.countdown, 1000);
        };
        self.stopCountdown = function () {
            if (self.countdown !== null) {
                return $interval.cancel(self.countdown);
            }
        };
        self.countdown = function () {
            if (self.time_remaining > 0) {
                return self.time_remaining -= 1000;
            } else {
                self.stopCountdown();
                //return invoiceService.notify("expired");
            }
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
                self.stopCountdown();
                return self.startCountdown();
            }
        };           
    }]);

