'use strict';

angular.module('myappApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0)) {
                    if (response.status == 404 && response.config.url.indexOf("/api/articles") != 0)
                    {
                        $rootScope.$emit('myappApp.httpError', "Article not found.");
                    }
                    else
                    {
                        $rootScope.$emit('myappApp.httpError', response);
                    }
	            }
                return $q.reject(response);
            }
        };
    });
