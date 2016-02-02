'use strict';

angular.module('myappApp')
    .factory('Sale', function ($resource, DateUtils) {
        return $resource('api/sales/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecha = DateUtils.convertLocaleDateFromServer(data.fecha);
                    data.printDate = DateUtils.convertLocaleDateFromServer(data.printDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fecha = DateUtils.convertLocaleDateToServer(data.fecha);
                    data.printDate = DateUtils.convertLocaleDateToServer(data.printDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fecha = DateUtils.convertLocaleDateToServer(data.fecha);
                    data.printDate = DateUtils.convertLocaleDateToServer(data.printDate);
                    return angular.toJson(data);
                }
            }
        });
    });
