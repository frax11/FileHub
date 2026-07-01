import { Component, EventEmitter, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileService } from '../../services/file-service';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './upload.html',
  styleUrl: './upload.css',
})
export class Upload {
  private fileService = inject(FileService);
  isLoading = signal<boolean>(false);

  @Output() uploadSuccess = new EventEmitter<void>();

  async onUploadFile(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    this.isLoading.set(true);
    var success;
    try {
      success = await this.fileService.uploadFile(input.files[0]);
    }catch (error){
      success = false;
    }
    if (success) {
      this.uploadSuccess.emit();
    } else {
      alert('Errore upload. (Max 50MB) ');
    }

    this.isLoading.set(false);
    input.value = '';
  }
}
