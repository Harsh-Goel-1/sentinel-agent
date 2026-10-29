#!/usr/bin/env bash
set -e

# Load .env file if present
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -f "$DIR/.env" ]; then
    set -a
    # shellcheck disable=SC1090
    source "$DIR/.env"
    set +a
elif [ -f .env ]; then
    set -a
    # shellcheck disable=SC1091
    source .env
    set +a
fi

if command -v mvn >/dev/null 2>&1; then
    exec mvn "$@"
elif [ -x "$HOME/.tools/apache-maven-3.9.16/bin/mvn" ]; then
    exec "$HOME/.tools/apache-maven-3.9.16/bin/mvn" "$@"
elif [ -x "/opt/homebrew/bin/mvn" ]; then
    exec "/opt/homebrew/bin/mvn" "$@"
else
    echo "Maven not found. Please install Maven or add it to PATH." >&2
    exit 1
fi
