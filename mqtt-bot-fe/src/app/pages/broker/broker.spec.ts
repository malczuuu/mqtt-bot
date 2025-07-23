import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Broker } from './broker';

describe('Broker', () => {
  let component: Broker;
  let fixture: ComponentFixture<Broker>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Broker]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Broker);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
