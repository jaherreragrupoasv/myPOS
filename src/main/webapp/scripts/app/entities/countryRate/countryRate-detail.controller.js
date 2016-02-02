'use strict';

angular.module('myappApp')
    .controller('CountryRateDetailController', function ($scope, $rootScope, $stateParams, entity, CountryRate) {
        $scope.countryRate = entity;
        $scope.load = function (id) {
            CountryRate.get({id: id}, function(result) {
                $scope.countryRate = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:countryRateUpdate', function(event, result) {
            $scope.countryRate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
