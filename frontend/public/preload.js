const { ipcRenderer, contextBridge } = require('electron')
const { getFromLocal } = require('./apis')

const api = {
  getFFMpegPath: () => ipcRenderer.invoke('getFFMpegPath'),
  encodeFFMpeg: async (filepath) => ipcRenderer.invoke('encodeFFMpeg', filepath),
  getFromLocal: async (filepath) => getFromLocal(filepath)
}

contextBridge.exposeInMainWorld('electron', api)
