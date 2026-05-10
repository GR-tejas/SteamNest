using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.StaticFiles;

namespace PC_Project.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class DownloadController : ControllerBase
    {
        [HttpGet("{*filePath}")]
        public IActionResult DownloadFile(string filePath)
        {
            string rootPath = ServerConfig.SharedFolderPath;

            string fullPath = Path.Combine(rootPath, filePath);

            if (!System.IO.File.Exists(fullPath))
            {
                return NotFound("File not found.");
            }

            var provider = new FileExtensionContentTypeProvider();

            if (!provider.TryGetContentType(fullPath, out string? contentType))
            {
                contentType = "application/octet-stream";
            }

            return PhysicalFile(
                fullPath,
                contentType,
                enableRangeProcessing: true
            );
        }
    }
}