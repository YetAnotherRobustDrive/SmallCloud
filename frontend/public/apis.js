
const which = require('which');
const path = require('path');
const { spawn } = require('child_process');
const { chdir } = require('process');
const { stat, readdir, readFile, existsSync} = require('fs');

async function
  getFFMpegPath() {
  return await which('ffmpeg', { nothrow: true });
}

async function
  encodeFFMpeg(ffmpegPath, resDir, filepath) {
  const filename = path.parse(filepath).name
  const initSegName = `init-${filename}$RepresentationID$.$ext$`
  const mediaSegName = `chunk-${filename}$RepresentationID$-$Number%05d$.$ext$`
  const outname = path.join(resDir, filename + '.mpd')
  const args = [`-i`, filepath, `-map`, `0`, `-c:a`, `aac`, `-c:v`, `libx264`,
    `-profile:v`, `main`, `-vf`, `scale=iw:ih`,
    `-bf`, `1`, `-keyint_min`, `60`, `-g`, `60`, `-sc_threshold`,
    `0`, `-b_strategy`, `0`, `-seg_duration`, `2`,
    `-use_timeline`, `1`, `-init_seg_name`, initSegName,
    `-media_seg_name`, mediaSegName, `-use_template`, `1`, `-f`,
    `dash`, outname]
  return await new Promise((resolve, reject) => {
    try {
      chdir(resDir)
      const ffmpeg = spawn(ffmpegPath, args)
      ffmpeg.stdout.on('data', (data) => {
        console.log(data.toString())
      })
      ffmpeg.stderr.on('data', (data) => {
        console.error(data.toString())
      })
      ffmpeg.on('close', (code) => {
        resolve(code);
      })
    } catch (e) {
      reject(e);
    }
  }).then((_) => {
    return new Promise((resolve, reject) => {
      stat(outname, (err, _) => {
        if (err !== null)
          reject(err);
        else
          resolve(outname);
      })
    })
  }).then((mpdPath) => {
    return new Promise((resolve, reject) => {
      readdir(resDir, (err, files) => {
        if (err !== null)
          reject(err);
        else
          resolve({
            success: true,
            mpdPath,
            files: files
              .filter((file) => file.startsWith(`init-${filename}`) ||
                file.startsWith(`chunk-${filename}`))
          });
      })
    })
  });
}

async function
  getFromLocal(filepath) {
  return new Promise((resolve, reject) => {
    try {
      if (!existsSync(filepath)) {
        reject(new Error('File not found'));
      }
      readFile(filepath, (err, data) => {
        if (err !== null)
          reject(err);
        else
          resolve(new Blob([data]));
      })
    }
    catch (e) {
      reject(e);
    }
  })}

exports.getFFMpegPath = getFFMpegPath;
exports.encodeFFMpeg = encodeFFMpeg;
exports.getFromLocal = getFromLocal;