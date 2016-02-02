'use strict';

angular.module('myappApp')
    .controller('ArticleController', function ($scope, $state, Article) {

        $scope.articles = [];
        $scope.loadAll = function() {
            Article.query(function(result) {
               $scope.articles = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.article = {
                articleName: null,
                barCode: null,
                price: null,
                tax: null,
                id: null
            };
        };
    });
