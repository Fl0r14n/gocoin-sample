'use strict';

var app = angular.module('app', ['service', 'directive']);

app.config(['$resourceProvider', function ($resourceProvider) {
        //!do not remove tailing / from urls
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }]);

var services = angular.module('service', ['ngResource']);

services.api = 'api/v1/';

services.endpoint = function (resource) {
    return services.api + resource + '/';
};

services.factory('GCPaymentService', function ($resource) {
    var url = services.endpoint('payment/gocoin');
    return {
        invoice: $resource(url, {}, {
            'create': {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        })
    };
});

var directives = angular.module('directive', []);

directives.directive('uiGoCoinButton', function () {
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
            'GCPaymentService',            
            function ($scope, gcPaymentService) {
                var self = this;

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
                    gcPaymentService.invoice.create(self.invoice, function (data) {
                        if (data) {
                            tabWindowId.location.href = data.gateway_url;
                        }
                    });
                };
                
                self.hasType = function() {
                    return !angular.isUndefined(self.invoice.price_currency);
                };
                
                self.isBitcoin = function() {
                    return self.invoice.price_currency.toUpperCase() === 'BTC';
                };
                
                self.isLitecoin = function() {
                    return self.invoice.price_currency.toUpperCase() === 'LTC';
                };
                
                self.isDogecoin = function() {
                    return self.invoice.price_currency.toUpperCase() === 'XDG';
                };
            }
        ],
        template: [
            '<a href="" target="_blank" ng-mousedown="gocoin.setUrl()" ng-model="gocoin.invoice" ng-if="gocoin.hasType()" >',
                '<img ng-if="gocoin.isBitcoin()" src="img/gocoin/btc_md_yes.png" alt="Bitcoin" />',
                '<img ng-if="gocoin.isLitecoin()" src="img/gocoin/ltc_md_yes.png" alt="Litecoin" />',
                '<img ng-if="gocoin.isDogecoin()" src="img/gocoin/xdg_md_yes.png" alt="Litecoin" />',
            '</a>'
        ].join('')
    };
});

