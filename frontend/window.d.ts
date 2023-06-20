<<<<<<< Updated upstream
import { EncodeResult } from './public/preload';

=======
import { EncodeResult } from './public/preload'
>>>>>>> Stashed changes

export type ElectronAPI = {
  getFFMpegPath: () => Promise<string | null>,
  encodeFFMpeg: (_: string) => Promise<EncodeResult | null>,
}

declare global {
  interface Window {
    electron: ElectronAPI;
  }  
}

