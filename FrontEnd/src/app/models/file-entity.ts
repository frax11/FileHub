export interface FileEntity {
  id: string;
  fileName: string;
  filePath: string;
  fileSize: number;
  fileType: string;
  ownerEmail: string;
  isShared: boolean;
  currentAccessCount: number;
  maxAccessCount: number;
  uploadDate: string;
}
