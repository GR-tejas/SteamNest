using PC_Project;

string[] commandLineArgs = args;

if (commandLineArgs.Length < 2)
{
    Console.WriteLine(
        "Missing server arguments."
    );

    return;
}

string port =
    commandLineArgs[0];

string folderPath =
    commandLineArgs[1];

var builder =
    WebApplication.CreateBuilder(args);

builder.WebHost.UseUrls(
    $"http://0.0.0.0:{port}"
);

builder.Services.AddControllers();

ServerConfig.SharedFolderPath =
    folderPath;

var app =
    builder.Build();

app.MapControllers();

app.Run();