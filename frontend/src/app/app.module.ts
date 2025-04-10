import { NgModule } from '@angular/core';
import { HashLocationStrategy, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule, routes } from './app-routing.module';
import { AppComponent } from './app.component'; // Standalone component
import { RouterModule } from '@angular/router';
import { MainComponent } from './components/views/main/main.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { HeroComponent } from './components/shared/hero/hero.component';
import { SearchbarComponent } from './components/shared/searchbar/searchbar.component';
import { PostsPreviewComponent } from './components/shared/posts-preview/posts-preview.component';
import { PostCardComponent } from './components/shared/post-card/post-card.component';
import { CtaComponent } from './components/shared/cta/cta.component';
import { AboutComponent } from './components/views/about/about.component';
import { HttpClientModule } from '@angular/common/http';
import { PostsComponent } from './components/views/posts/posts.component';
import { SuggestionComponent } from './components/shared/suggestion/suggestion.component';
import { PostDetailComponent } from './components/views/post-detail/post-detail.component';
import { LoginComponent } from './components/views/login/login.component';
import { RegisterComponent } from './components/views/register/register.component';
import { ProductsComponent } from './components/views/products/products.component';
import { FormsModule } from '@angular/forms';
import { ProductCardComponent } from './components/shared/product-card/product-card.component';
import { ThemeToggleComponent } from './components/shared/theme-toggle/theme-toggle.component';
import { PostCommentComponent } from './components/shared/post-comment/post-comment.component';
import { ProductDetailsComponent } from './components/views/products-details/products-details.component';
import { LoaderComponent } from './components/shared/loader/loader.component';
import { CompareTableComponent } from './components/shared/compare-table/compare-table.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ProfileComponent } from './components/views/profile/profile.component';
import { SettingsComponent } from './components/views/settings/settings.component';
import { ShoppingListDetailsComponent } from './components/shared/shopping-list-detail/shopping-list-detail.component';
import { CreateShoppingListComponent } from './components/shared/new-shopping-list/new-shopping-list.component';
import { AdminComponent } from './components/views/admin/admin.component';
import { AddListPopupComponent } from './components/shared/add-to-list-popup/add-to-list-popup.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        FooterComponent,
        MainComponent,
        HeroComponent,
        SearchbarComponent,
        PostsPreviewComponent,
        PostCardComponent,
        CtaComponent,
        AboutComponent,
        PostsComponent,
        SuggestionComponent,
        PostDetailComponent,
        LoginComponent,
        RegisterComponent,
        ProductsComponent,
        PostDetailComponent,
        ProductCardComponent,
        ThemeToggleComponent,
        PostCommentComponent,
        ProductDetailsComponent,
        LoaderComponent,
        CompareTableComponent,
        ProfileComponent,
        SettingsComponent,
        ShoppingListDetailsComponent,
        CreateShoppingListComponent,
        AdminComponent,
        AddListPopupComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        RouterModule.forRoot(routes, {
            scrollPositionRestoration: "top"
        }),
        HttpClientModule,
        FormsModule,
        NgbModule],
    providers: [],
    bootstrap: [AppComponent] // Bootstrap the standalone component
})
export class AppModule { }
