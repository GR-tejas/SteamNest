using System.Net;
using System.Net.Sockets;

using System.Diagnostics;

using System.ComponentModel;

namespace FileShareAndStreamServer__Windows_
{
    public partial class Form1 : Form
    {
        private Process? serverProcess;
        public Form1()
        {
            InitializeComponent();

            if (IsInDesignMode())
            {
                return;
            }
        }

        private bool IsInDesignMode()
        {
            return LicenseManager.UsageMode == LicenseUsageMode.Designtime;
        }

        private void Form1_Load( object sender, EventArgs e)
        {
            submitButton.Enabled = true;

            stopButton.Enabled = false;
        }

        private string GetLocalIpAddress()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());

            foreach (var ip in host.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    return ip.ToString();
                }
            }

            return "IP NOT FOUND";
        }

        private void portTextBox_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar))
            {
                e.Handled = true;
            }
        }

        private void submitButton_Click(object sender,EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(portTextBox.Text))
            {
                errorLabel.Text = "Port number is required.";

                return;
            }

            if (string.IsNullOrWhiteSpace(folderPathLabel.Text))
            {
                errorLabelFolder.Text = "Folder location is required.";

                return;
            }

            errorLabelFolder.Text = "";

            int portNumber;

            bool isValidNumber = int.TryParse(portTextBox.Text, out portNumber);

            if (!isValidNumber || portNumber < 2000 || portNumber > 65535)
            {
                errorLabel.Text = "Invalid port. Please enter a port number between 2000 and 65535.";

                return;
            }

            errorLabel.Text = "";

            string serverPath = Path.Combine(Application.StartupPath, "StreamNestServer.exe");

            serverProcess = new Process();

            serverProcess.StartInfo.FileName = serverPath;

            serverProcess.StartInfo.Arguments = $"{portNumber} \"{folderPathLabel.Text}\"";

            serverProcess.Start();

            submitButton.Enabled = false;

            stopButton.Enabled = true;

            serverStatusLabel.Text = "Server Running";

            serverStatusLabel.ForeColor = Color.Green;

            portTextBox.ReadOnly = true;

            string ipAddress = GetLocalIpAddress();

            ipLabel.Text = $"IP Address: {ipAddress}";

            portLabel.Text = $"Port: {portNumber}";

            ServerConfig.SharedFolderPath = folderPathLabel.Text;

            MessageBox.Show(
                $"Server Running\n\n" +
                $"IP Address: {ipAddress}\n" +
                $"Port: {portNumber}\n" +
                $"Folder: {folderPathLabel.Text}"
            );
        }

        private void browseButton_Click(object sender, EventArgs e)
        {
            using (FolderBrowserDialog folderDialog = new FolderBrowserDialog())
            {
                DialogResult result = folderDialog.ShowDialog();

                if (result == DialogResult.OK)
                {
                    folderPathLabel.Text = folderDialog.SelectedPath;
                }
            }
        }

        private void stopButton_Click(object sender, EventArgs e)
        {
            if (serverProcess != null && !serverProcess.HasExited)
            {
                serverProcess.Kill();

                serverProcess.Dispose();

                serverProcess = null;

                submitButton.Enabled = true;

                stopButton.Enabled = false;

                portTextBox.ReadOnly = false;

                ipLabel.Text = "IP Address: ";

                portLabel.Text = "Port: ";
            }
        }
    }
}