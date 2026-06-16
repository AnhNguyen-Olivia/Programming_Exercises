$connections = @()
1..1000 | ForEach-Object {
    try {
        $tcp = New-Object System.Net.Sockets.TcpClient
        $tcp.Connect("localhost", 9020)
        $connections += $tcp
        Write-Host "Connected: $_"
    } catch {
        Write-Host "Failed at $_`: $_"
    }
}

Write-Host "Done. Press Enter to close all connections."
Read-Host
$connections | ForEach-Object { $_.Close() }