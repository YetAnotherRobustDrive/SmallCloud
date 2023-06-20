import { EncodeResult } from './public/preload';

export type ElectronAPI = {
  getFFMpegPath: () => Promise<string | null>,
  encodeFFMpeg: (_: string) => Promise<EncodeResult | null>,
  getFromLocal: () => Promise<string | null>,
}

declare global {
  interface Window {
    electron: ElectronAPI;
  }  
}

