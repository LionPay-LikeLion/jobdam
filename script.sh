#!/bin/bash

base_dir="src/main/java/com/jobdam"

modules=(admin user community sns payment)
layers=(controller service dto entity repository mapper)

# Create module folders with .gitkeep
for module in "${modules[@]}"; do
  for layer in "${layers[@]}"; do
    dir="$base_dir/$module/$layer"
    mkdir -p "$dir"
    touch "$dir/.gitkeep"
  done
done

# Create common structure
common_layers=(exception config util base)
for layer in "${common_layers[@]}"; do
  dir="$base_dir/common/$layer"
  mkdir -p "$dir"
  touch "$dir/.gitkeep"
done

echo "✅ Folder structure with .gitkeep files created under $base_dir"
