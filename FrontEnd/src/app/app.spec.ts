import { TestBed } from '@angular/core/testing';
import { app } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [app],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(app);
    const rootapp = fixture.componentInstance;
    expect(rootapp).toBeTruthy();
  });

  it('should render title', async () => {
    const fixture = TestBed.createComponent(app);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, FrontEnd');
  });
});
