'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('sale', {
                parent: 'entity',
                url: '/sales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.sale.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sale/sales.html',
                        controller: 'SaleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sale');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sale.detail', {
                parent: 'entity',
                url: '/sale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.sale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sale/sale-detail.html',
                        controller: 'SaleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('sale');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Sale', function($stateParams, Sale) {
                        return Sale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('sale.new', {
                parent: 'sale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.sale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sale/sale-dialog.html',
                        controller: 'SaleDialogController'
                    }
                }
            })
            //.state('sale.new', {
            //    parent: 'sale',
            //    url: '/new',
            //    data: {
            //        authorities: [],
            //    },
            //    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
            //
            //        $uibModal.open({
            //            templateUrl: 'scripts/app/entities/sale/sale-dialog.html',
            //            controller: 'SaleDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: function () {
            //                    return {
            //                        fecha: null,
            //                        country: null,
            //                        rate: null,
            //                        subTotal: null,
            //                        discounts: null,
            //                        taxes: null,
            //                        total: null,
            //                        totalPaied: null,
            //                        printDate: null,
            //                        id: null
            //                    };
            //                }
            //            }
            //        }).result.then(function(result) {
            //            $state.go('sale', null, { reload: true });
            //        }, function() {
            //            $state.go('sale');
            //        })
            //    }]
            //})
            .state('sale.edit', {
                parent: 'sale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sale/sale-dialog.html',
                        controller: 'SaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Sale', function(Sale) {
                                return Sale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('sale.pay', {
                parent: 'sale',
                url: '/{id}/pay',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/sale/sale-pay.html',
                        controller: 'SalePayController',
                        resolve: {
                            entity: ['Sale', function(Sale) {
                                return Sale.get({id : $stateParams.id});
                            }]
                        }
                    }
                }
            })
            .state('sale.delete', {
                parent: 'sale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/sale/sale-delete-dialog.html',
                        controller: 'SaleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Sale', function(Sale) {
                                return Sale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('sale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
