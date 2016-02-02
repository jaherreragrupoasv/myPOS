'use strict';

describe('SaleLine Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSaleLine, MockSale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSaleLine = jasmine.createSpy('MockSaleLine');
        MockSale = jasmine.createSpy('MockSale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'SaleLine': MockSaleLine,
            'Sale': MockSale
        };
        createController = function() {
            $injector.get('$controller')("SaleLineDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myappApp:saleLineUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
