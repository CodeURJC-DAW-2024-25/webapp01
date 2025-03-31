
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