import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
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
    AboutComponent
  ], 
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
  ],
  providers: [],
  bootstrap: [AppComponent] // Bootstrap the standalone component
})
export class AppModule { }
