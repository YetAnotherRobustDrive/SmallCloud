let appContext = {
  ffmpegPath: null,
  dataDir: null
}

export function getFFMpegPath() {
  return appContext.ffmpegPath;
}

export function getDataDir() {
  return appContext.dataDir;
}

export function setFFMpegPath(path) {
  appContext.ffmpegPath = path;
}

export function setDataDir(dir) {
  appContext.dataDir = dir;
}
