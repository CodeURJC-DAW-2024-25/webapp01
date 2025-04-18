import { AuthService } from '@/services/auth.service';
import { CreatePostRequest } from '@/types/Posts';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostsService } from 'src/app/services/post.service';

@Component({
  selector: 'app-create-post-form',
  templateUrl: './create-post-form.component.html'
})
export class CreatePostFormComponent implements OnInit {
  isAdmin: boolean = false;
  post: any;
  tags: string[] = ['Ahorro', 'Finanzas', 'Supermercado', 'Producto', 'Tip'];
  postForm!: FormGroup;
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private postService: PostsService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.isAdmin();

    this.postForm = this.fb.group({
      title: ['', Validators.required],
      visibility: ['PUBLIC'],
      description: [''],
      author: [''],
      tags: [[]],
      content: ['']
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onSubmit(): void {
    if (!this.isAdmin || this.postForm.invalid) return;

    const formValues = this.postForm.value;

    const postRequest: CreatePostRequest = {
      title: formValues.title,
      description: formValues.description,
      content: formValues.content,
      author: formValues.author,
      date: new Date().toISOString(),
      visibility: formValues.visibility,
      tags: formValues.tags
    };

    this.postService.createPost(postRequest).subscribe({
      next: (response) => {
        console.log('Post creado exitosamente:', response);
        this.router.navigate(['/admin']);
      },
      error: (error) => {
        console.error('Error al crear el post:', error);
      }
    });
  }


}
