'use strict';

angular.module('myappApp')
    .factory('Article', function ($resource, DateUtils) {
        var mainUrl = 'api/articles/:id'
        var params = {id: '@_id', barCode: '@_barCode'}

        return $resource(mainUrl,params,
            {
                'query': {method: 'GET', isArray: true},
                'get':   {method: 'GET',
                          transformResponse: function (data) {data = angular.fromJson(data);
                          return data;
                }},
                'update': {method: 'PUT'},
                'getByBarCode': {url: 'api/articlesByBarCode/:barCode', method: 'GET',
                    transformResponse: function (data) {

                        if (data != "") {
                            data = angular.fromJson(data);
                            return data;
                        }
                    },
                    interceptor: {
                        responseError: function(e) {
                            console.warn('Articulo no encontrado', e);
                        }
                    }
                }
        });
    });
