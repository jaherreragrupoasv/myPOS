'use strict';

describe('Sale Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSale, MockSaleLine;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSale = jasmine.createSpy('MockSale');
        MockSaleLine = jasmine.createSpy('MockSaleLine');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Sale': MockSale,
            'SaleLine': MockSaleLine
        };
        createController = function() {
            $injector.get('$controller')("SaleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myappApp:saleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
