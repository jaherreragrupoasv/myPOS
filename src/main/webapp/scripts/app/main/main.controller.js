'use strict';

angular.module('myappApp')
    .controller('MainController', function ($scope, Principal, Auth) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };

    });
