namespace FileShareAndStreamServer__Windows_
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            submitButton = new Button();
            errorLabel = new Label();
            portTextBox = new TextBox();
            label1 = new Label();
            browseButton = new Button();
            label2 = new Label();
            folderPathLabel = new Label();
            errorLabelFolder = new Label();
            stopButton = new Button();
            serverStatusLabel = new Label();
            ipLabel = new Label();
            portLabel = new Label();
            SuspendLayout();
            // 
            // submitButton
            // 
            submitButton.Location = new Point(160, 270);
            submitButton.Name = "submitButton";
            submitButton.Size = new Size(75, 23);
            submitButton.TabIndex = 2;
            submitButton.Text = "Start Server";
            submitButton.UseVisualStyleBackColor = true;
            submitButton.Click += submitButton_Click;
            // 
            // errorLabel
            // 
            errorLabel.AutoSize = true;
            errorLabel.ForeColor = Color.Red;
            errorLabel.Location = new Point(160, 124);
            errorLabel.Name = "errorLabel";
            errorLabel.Size = new Size(0, 15);
            errorLabel.TabIndex = 3;
            // 
            // portTextBox
            // 
            portTextBox.Location = new Point(160, 98);
            portTextBox.Name = "portTextBox";
            portTextBox.Size = new Size(100, 23);
            portTextBox.TabIndex = 4;
            portTextBox.KeyPress += portTextBox_KeyPress;
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new Point(160, 80);
            label1.Name = "label1";
            label1.Size = new Size(59, 15);
            label1.TabIndex = 5;
            label1.Text = "Enter Port";
            // 
            // browseButton
            // 
            browseButton.Location = new Point(160, 176);
            browseButton.Name = "browseButton";
            browseButton.Size = new Size(75, 23);
            browseButton.TabIndex = 6;
            browseButton.Text = "Browse";
            browseButton.UseVisualStyleBackColor = true;
            browseButton.Click += browseButton_Click;
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Location = new Point(160, 158);
            label2.Name = "label2";
            label2.Size = new Size(74, 15);
            label2.TabIndex = 7;
            label2.Text = "Select Folder";
            // 
            // folderPathLabel
            // 
            folderPathLabel.AutoSize = true;
            folderPathLabel.Location = new Point(160, 202);
            folderPathLabel.Name = "folderPathLabel";
            folderPathLabel.Size = new Size(0, 15);
            folderPathLabel.TabIndex = 8;
            // 
            // errorLabelFolder
            // 
            errorLabelFolder.AutoSize = true;
            errorLabelFolder.ForeColor = Color.Red;
            errorLabelFolder.Location = new Point(160, 217);
            errorLabelFolder.Name = "errorLabelFolder";
            errorLabelFolder.Size = new Size(0, 15);
            errorLabelFolder.TabIndex = 9;
            // 
            // stopButton
            // 
            stopButton.Location = new Point(159, 299);
            stopButton.Name = "stopButton";
            stopButton.Size = new Size(75, 23);
            stopButton.TabIndex = 10;
            stopButton.Text = "Stop Server";
            stopButton.UseVisualStyleBackColor = true;
            stopButton.Click += stopButton_Click;
            // 
            // serverStatusLabel
            // 
            serverStatusLabel.AutoSize = true;
            serverStatusLabel.ForeColor = Color.Red;
            serverStatusLabel.Location = new Point(628, 46);
            serverStatusLabel.Name = "serverStatusLabel";
            serverStatusLabel.Size = new Size(86, 15);
            serverStatusLabel.TabIndex = 11;
            serverStatusLabel.Text = "Server Stopped";
            // 
            // ipLabel
            // 
            ipLabel.AutoSize = true;
            ipLabel.Location = new Point(567, 61);
            ipLabel.Name = "ipLabel";
            ipLabel.Size = new Size(68, 15);
            ipLabel.TabIndex = 12;
            ipLabel.Text = "IP Address: ";
            // 
            // portLabel
            // 
            portLabel.AutoSize = true;
            portLabel.Location = new Point(567, 80);
            portLabel.Name = "portLabel";
            portLabel.Size = new Size(35, 15);
            portLabel.TabIndex = 13;
            portLabel.Text = "Port: ";
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(800, 450);
            Controls.Add(portLabel);
            Controls.Add(ipLabel);
            Controls.Add(serverStatusLabel);
            Controls.Add(stopButton);
            Controls.Add(errorLabelFolder);
            Controls.Add(folderPathLabel);
            Controls.Add(label2);
            Controls.Add(browseButton);
            Controls.Add(label1);
            Controls.Add(portTextBox);
            Controls.Add(errorLabel);
            Controls.Add(submitButton);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            ResumeLayout(false);
            PerformLayout();
        }

        #endregion
        private Button submitButton;
        private Label errorLabel;
        private TextBox portTextBox;
        private Label label1;
        private Button browseButton;
        private Label label2;
        private Label folderPathLabel;
        private Label errorLabelFolder;
        private Button stopButton;
        private Label serverStatusLabel;
        private Label ipLabel;
        private Label portLabel;
    }
}
