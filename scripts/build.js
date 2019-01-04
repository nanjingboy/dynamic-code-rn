const klaw = require('klaw');
const path = require('path');
const chalk = require('chalk');
const fs = require('fs-extra');
const { exec } = require('shelljs');
const archiver = require('archiver');
const sourceRootPath = path.join(__dirname, '..', 'src');
const buildDistRootPath = path.join(__dirname, '..', 'build');

function zipBundle(rootPath, platform) {
  const sourcePath = path.join(rootPath, platform);
  const output = fs.createWriteStream(path.join(rootPath, `${platform}.zip`));
  output.on('close', () => {
    fs.removeSync(sourcePath);
  });
  const archive = archiver('zip');
  archive.pipe(output);
  archive.directory(sourcePath, false);
  archive.finalize();
}

async function buildBundle(dirPath) {
  const entryJSPath = path.join(dirPath, 'index.js');
  const exists = await fs.pathExists(entryJSPath);
  if (exists) {
    const version = path.basename(dirPath);
    const buildDistPath = path.join(buildDistRootPath, version);
    await fs.ensureDir(buildDistPath);
    await fs.emptyDir(buildDistPath);

    console.log(chalk.green(`Start create android bundle for V${version}...`));
    const androidDistPath = path.join(buildDistPath, 'android');
    await fs.ensureDir(androidDistPath);
    exec(
      `react-native bundle --platform android --dev false \
        --entry-file ${entryJSPath} \
        --bundle-output ${path.join(androidDistPath, `index.bundle`)} \
        --assets-dest ${androidDistPath}`,
      { silent: true }
    );
    zipBundle(buildDistPath, 'android');

    console.log(chalk.green(`Start create ios bundle for V${version}...`));
    const iosDistPath = path.join(buildDistPath, 'ios');
    await fs.ensureDir(iosDistPath);
    exec(
      `react-native bundle --platform ios --dev false \
        --entry-file ${entryJSPath} \
        --bundle-output ${path.join(iosDistPath, `index.bundle`)} \
        --assets-dest ${iosDistPath}`,
      { silent: true }
    );
    zipBundle(buildDistPath, 'ios');
  }
}

function buildBundles() {
  return new Promise((resolve, reject) => {
    klaw(sourceRootPath).on('data', file => {
      if (file.stats.isDirectory()) {
        buildBundle(file.path);
      }
    }).on('end', () => {
      resolve();
    })
  });
}

async function run() {
  await buildBundles();
}

run();