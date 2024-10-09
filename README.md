## mc-registry-dump

Use environment variable `MC_REGISTRY_DUMP_OUTPUT_DIR` to supply the path to the output directory (default: `./output`),
e.g. `MC_REGISTRY_DUMP_OUTPUT_DIR=../output`

Run `./gradlew runServer` to trigger registry dump for all supported MC versions (currently 1.14.3+)

The registry dump will be written to `<output_dir>/<mc_version>.json`, see also: [output](output) directory in this repos
