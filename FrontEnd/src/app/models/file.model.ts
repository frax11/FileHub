export interface FileModel {
  id: string;
  fileName: string;
  fileSize: number;
  fileType: string;
  uploadDate: string;
  isShared: boolean;
  remainingAccesses: number;
}

export interface SharedFile {
  id: string;
  fileId: string;
  fileName: string;
  ownerName: string;
  sharedAt: string;
  remainingAccesses: number;
  maxAccessCount: number;
}
