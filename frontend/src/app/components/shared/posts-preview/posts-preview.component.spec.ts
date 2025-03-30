import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostsPreviewComponent } from './posts-preview.component';

describe('PostsPreviewComponent', () => {
  let component: PostsPreviewComponent;
  let fixture: ComponentFixture<PostsPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostsPreviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
