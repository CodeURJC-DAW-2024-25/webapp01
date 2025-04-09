import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateShoppingListComponent } from './new-shopping-list.component';

describe('NewShoppingListComponent', () => {
    let component: CreateShoppingListComponent;
    let fixture: ComponentFixture<CreateShoppingListComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CreateShoppingListComponent],
        }).compileComponents();

        fixture = TestBed.createComponent(CreateShoppingListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
