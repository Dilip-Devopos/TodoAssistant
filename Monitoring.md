# Kubernetes Monitoring with Prometheus & Grafana (Helm)

This document explains how to install **Prometheus and Grafana** on a Kubernetes cluster using **Helm**.  
It uses the `kube-prometheus-stack`, which comes with **preconfigured dashboards**.

---

## Prerequisites

- Kubernetes cluster (Minikube / Rancher Desktop / EKS / etc.)
- `kubectl` configured
- `helm` installed

---

## Step 1: Add Prometheus Helm Repository

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
Step 2: Install Prometheus + Grafana
helm install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace
⏳ Wait 2–5 minutes for all components to start.

Step 3: Verify Pods Status
kubectl get pods -n monitoring
✔️ All pods should be in Running or Completed state.

Step 4: Access Grafana
Get Grafana Admin Password
kubectl get secret monitoring-grafana -n monitoring \
  -o jsonpath="{.data.admin-password}" | base64 --decode
Port-Forward Grafana Service
kubectl port-forward -n monitoring svc/monitoring-grafana 3000:80
Open in Browser
http://localhost:3000
Login Details

Username: admin

Password: Retrieved from the command above

Step 5: Preinstalled Dashboards 🎉
Grafana comes with built-in dashboards.
No manual import is required.

Navigate to:

Dashboards → Browse → Kubernetes
Available Dashboards
Kubernetes Cluster Overview

Nodes

Pods

Deployments

StatefulSets

Prometheus Metrics

Step 6: Access Prometheus (Optional)
kubectl port-forward -n monitoring svc/monitoring-kube-prometheus-prometheus 9090
Open in browser:

http://localhost:9090
Optional: Grafana Persistence
To persist Grafana data across restarts, configure a Persistent Volume.
(Depends on Docker / containerd and storage size requirements.)

Summary
Installed full monitoring stack using Helm

Prometheus collects metrics

Grafana dashboards
