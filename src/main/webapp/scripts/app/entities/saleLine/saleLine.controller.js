'use strict';

angular.module('myappApp')
    .controller('SaleLineController',
        ['$scope', '$stateParams', 'entity', 'SaleLine', 'Sale', 'Article',
            function ($scope, $stateParams, entity, SaleLine, Sale, Article) {

                $scope.sale = entity;
                $scope.articles = Article.query();

                $scope.load = function (id) {
                    Sale.get({id: id}, function (result) {
                        $scope.sale = result;
                    });
                };
                $scope.load();

                $scope.refresh = function () {
                    $scope.load();
                    $scope.clear();
                };

                $scope.clear = function () {
                    $scope.saleLine = {
                        quantity: null,
                        price: null,
                        tax: null,
                        id: null,
                        article: null,
                        sale: null
                    };
                };

                $scope.save = function () {
                    $scope.isSaving = true;

                    if ($scope.sale.id = null) {

                        $scope.sale.fecha = Date.now();
                        $scope.sale.country = "ES";

                        Sale.update($scope.sale, onSaveSuccessSale, onSaveErrorSale);
                    };

                    if ($scope.saleLine.id != null) {
                        SaleLine.update($scope.saleLine, onSaveSuccess, onSaveError);
                    } else {
                        $scope.saleLine.sale_id = $scope.sale;
                        SaleLine.save($scope.saleLine, onSaveSuccess, onSaveError);
                    }
                };

                var onSaveSuccess = function (result) {
                    $scope.isSaving = false;
                    $scope.saleLines.push(result);
                    $scope.clear();
                    $('#field_article').focus();
                };

                var onSaveError = function (result) {
                    $scope.isSaving = false;
                };

                var onSaveSuccessSale = function (result) {
                    $scope.isSaving = false;
                    $scope.sale = result;
                };

                var onSaveErrorSale = function (result) {
                    $scope.isSaving = false;
                };

            }]);
