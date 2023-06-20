const { ipcRenderer, contextBridge } = require("electron")


const api = {
  getFFMpegPath: () => ipcRenderer.invoke('getFFMpegPath'),
  encodeFFMpeg: async (filepath) => ipcRenderer.invoke('encodeFFMpeg', {filepath})
}

contextBridge.exposeInMainWorld('electron', api)
