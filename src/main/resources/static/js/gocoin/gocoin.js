'use strict';

angular.module('gocoin', ['ngResource', 'ui.bootstrap', 'monospaced.qrcode']);

angular.module('gocoin').factory('GoCoinService', ['$resource', function($resource) {
    var self = this;
    self.api = 'api/v1/';
    self.endpoint = function(resource) {
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

angular.module('gocoin').controller('GoCoinController', ['$scope', function($scope) {
    var self = this;
    self.showBTCPayment = false;
    self.showLTCPayment = false;
    self.showXDGPayment = false;
    
    self.showPayment = function(type) {
        
    };
    
}]);

