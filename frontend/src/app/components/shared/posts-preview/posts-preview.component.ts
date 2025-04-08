import { Component, inject, Input, input } from "@angular/core";
import { Post } from "@/types/Posts";

@Component({
	selector: "app-posts-preview",
	templateUrl: "./posts-preview.component.html",
	styleUrl: "./posts-preview.component.css"
})
export class PostsPreviewComponent {
	@Input() posts: Post[] = [];
	@Input() extendedClass: string = "";
}
