import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private readonly API_URL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}
  loadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.API_URL}/file/upload`, formData);
  }

  // LISTA DEI MIEI FILE
  getMyFiles(): Observable<File[]> {
    return this.http.get<File[]>(`${this.API_URL}/file/all`);
  }

  // CANCELLA FILE
  deleteFile(fileId: string): Observable<any> {
    return this.http.post(`${this.API_URL}/file/`,fileId);
  }

  // DOWNLOAD FILE
  downloadFile(fileId: string): Observable<Blob> {
    return this.http.get(`${this.API_URL}/files/download/${fileId}`, {
      responseType: 'blob', // ← importante per ricevere un file binario
    });
  }
}
