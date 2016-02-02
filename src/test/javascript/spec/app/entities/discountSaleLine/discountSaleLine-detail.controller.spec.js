'use strict';

describe('DiscountSaleLine Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockDiscountSaleLine;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockDiscountSaleLine = jasmine.createSpy('MockDiscountSaleLine');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'DiscountSaleLine': MockDiscountSaleLine
        };
        createController = function() {
            $injector.get('$controller')("DiscountSaleLineDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myappApp:discountSaleLineUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
