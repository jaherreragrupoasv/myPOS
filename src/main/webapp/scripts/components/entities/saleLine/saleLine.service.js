'use strict';

angular.module('myappApp')
    .factory('SaleLine', function ($resource, DateUtils) {
        return $resource('api/saleLines/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
