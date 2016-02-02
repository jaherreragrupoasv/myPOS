'use strict';

angular.module('myappApp')
    .controller('CountryRateController', function ($scope, $state, CountryRate) {

        $scope.countryRates = [];
        $scope.loadAll = function() {
            CountryRate.query(function(result) {
               $scope.countryRates = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.countryRate = {
                country: null,
                rate: null,
                id: null
            };
        };
    });
