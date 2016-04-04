'use strict';

angular.module('myappApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, $http, CountryRate) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;

                //Traer precios
                //$http
                //    .get("http://apilayer.net/api/live?access_key=0c38dc72256724ec14cd4cbaebba940b", {dataType: 'jsonp'})
                //    .success(function (data) {
                //
                //        console.log(data);
                //        //deferred.resolve(data);
                //    })
                //    .error(function (data) {
                //        console.log(data);
                //        //deferred.resolve([]);
                //    });

                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
