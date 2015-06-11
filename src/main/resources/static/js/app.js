'use strict';

var app = angular.module('app', ['gocoin', 'utils']);

app.config(['$resourceProvider', function ($resourceProvider) {
        //!do not remove tailing / from urls
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }]);

angular.module('utils', ['ngResource']);

angular.module('utils').provider('RestResource', function () {
    self = this;
    self.api = '';
    self.endpoint = function(resource) {
        return self.api + resource + '/';
    };    
    return {
        api: function (path) {
            self.api = path;
        },
        $get: ['$resource', function ($resource) {
                return {
                    endpoint: self.endpoint,
                    $rest: function(resource) {
                        return $resource(self.endpoint(resource) + ':id/', {id: '@id'});
                    },
                    $resource: $resource
                };
            }]
    };
});

angular.module('utils').config(['RestResourceProvider', function (restProvider) {
        restProvider.api('api/v1/');
    }]);