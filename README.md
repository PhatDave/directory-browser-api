# directory-browser-api

This project is meant for quick directory crawling/parsing.

The endpoint /dir returns a list of folders in the directory with associated stats such as size, modification times, access times etc.

These stats are gathered from files (and folders) in the directory recursively and summed up to their parent folder.

For example a folder with 2 items whose sizes are 100MB will have a size of 200MB.

A folder with 2 items of which one was modified on the 20-02-2023 and the other on the 19-02-2023 the folder would have an oldestModify of 19-02-2023 and newestModify of 20-02-2023.
