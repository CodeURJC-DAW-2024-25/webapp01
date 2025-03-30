import { Component, inject } from '@angular/core';
import { UserDataService } from '../../../services/templates/user-data.service';

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.css'
})
export class HeroComponent {
  userData: UserDataService = inject(UserDataService);
}
