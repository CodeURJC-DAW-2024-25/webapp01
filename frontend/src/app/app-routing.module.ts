import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/views/main/main.component';
import { AboutComponent } from './components/views/about/about.component';
import { PostsComponent } from './components/views/posts/posts.component';
import { ProductsComponent } from './components/views/products/products.component';
import { PostDetailComponent } from './components/views/post-detail/post-detail.component';
import { LoginComponent } from './components/views/login/login.component';
import { RegisterComponent } from './components/views/register/register.component';
import { ProductDetailsComponent } from './components/views/products-details/products-details.component';
import { ProfileComponent } from './components/views/profile/profile.component';

export const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'about', component: AboutComponent },
  { path: 'posts', component: PostsComponent },
  { path: 'posts/:id', component: PostDetailComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent  },
  { path: 'products/:id', component: ProductDetailsComponent },
  { path: 'profile', component: ProfileComponent }, 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
