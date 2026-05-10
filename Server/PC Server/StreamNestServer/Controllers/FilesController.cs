using Microsoft.AspNetCore.Mvc;
using System.IO;
using System.Linq;

namespace PC_Project.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class FilesController : ControllerBase
    {
        [HttpGet("{*folderPath}")]
        public IActionResult GetFiles(string? folderPath)
        {
            string rootPath = ServerConfig.SharedFolderPath;

            string fullPath = rootPath;

            if (!string.IsNullOrEmpty(folderPath))
            {
                fullPath = Path.Combine(rootPath, folderPath);
            }

            if (!Directory.Exists(fullPath))
            {
                return NotFound("Folder does not exist.");
            }

            var folders = Directory.GetDirectories(fullPath)
                .Select(folder => new
                {
                    Name = Path.GetFileName(folder),
                    Type = "Folder"
                });

            string[] allowedExtensions =
            {
                ".mp4",
                ".mkv",
                ".mov",
                ".avi",
                ".jpg",
                ".jpeg",
                ".png",
                ".webp",
                ".txt"
            };

            var files = Directory.GetFiles(fullPath)
                .Where(file =>
                    allowedExtensions.Contains(
                        Path.GetExtension(file).ToLower()))
                .Select(file => new
                {
                    Name = Path.GetFileName(file),
                    Type = "File"
                });

            var result = folders.Concat(files);

            return Ok(result);
        }
    }
}