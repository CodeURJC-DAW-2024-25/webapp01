
export interface Post {
    id: number;
    title: string;
    description: string;
    author?: string;
    date: string;
    readingTime: string;
    visibility: 'PUBLIC' | 'PRIVATE';
    tags: string[];
}

export interface ContentPost extends Post {
    content: string;
}

export interface CreatePostRequest {
    title: string;
    description: string;
    content: string;
    author: string;
    date: string;
    visibility: string;
    tags: string[];
  }
