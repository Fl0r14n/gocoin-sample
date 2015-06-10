'use strict';

var app = angular.module('app', ['gocoin']);

app.config(['$resourceProvider', function ($resourceProvider) {
        //!do not remove tailing / from urls
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }]);
