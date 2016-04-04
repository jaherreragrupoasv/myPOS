'use strict';

angular.module('myappApp')
    .factory('Sale', function ($resource, DateUtils) {
        var mainUrl = 'api/sales/:id'
        var params = {id: '@_id'}

        return $resource(mainUrl, params, {
            'query': {method: 'GET', isArray: true},
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
            },
            'print': {
                url: 'api/sales/print/:id',
                method: 'GET'
            },
            'getLines': {url: 'api/sales/saleLines/:id',method: 'GET', isArray: true}
        });
    });

    //.factory('SaleGetLines', function ($resource) {
    //    return $resource('api/sales/saleLines/:id', {}, {
    //        'getLines': {
    //            method: {method: 'GET', isArray: true}
    //        }
    //    })
    //});

