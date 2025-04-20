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
import { AdminComponent } from './components/views/admin/admin.component';
import { ProfileComponent } from './components/views/profile/profile.component';
import { SettingsComponent } from './components/views/settings/settings.component';
import { authGuard } from './guards/auth.guard';
import { ShoppingListDetailsComponent } from './components/views/shopping-list-detail/shopping-list-detail.component';
import { CreateShoppingListComponent } from './components/shared/new-shopping-list/new-shopping-list.component';
import { CreatePostFormComponent } from './components/views/create-post-form/create-post-form.component';

export const roles = {
  admin: 'ADMIN',
  user: 'USER'
}

export const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'about', component: AboutComponent },
  { path: 'posts', component: PostsComponent },
  { path: 'posts/:id', component: PostDetailComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products/:id', component: ProductDetailsComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard], data: { roles: [roles.user, roles.admin] } },
  { path: 'settings', component: SettingsComponent, canActivate: [authGuard], data: { roles: [roles.user, roles.admin] } },
  { path: 'shoppingList/:id', component: ShoppingListDetailsComponent, canActivate: [authGuard], data: { roles: [roles.user, roles.admin] }  },
  { path: 'create-list', component: CreateShoppingListComponent },
  { path: 'admin', component: AdminComponent, canActivate: [authGuard], data: { roles: [roles.admin] } },
  { path: 'createPost', component: CreatePostFormComponent, canActivate: [authGuard], data: { roles: [ roles.admin] } },
  { path: 'editPost/:id', component: CreatePostFormComponent, canActivate: [authGuard], data: { roles: [ roles.admin] } },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
