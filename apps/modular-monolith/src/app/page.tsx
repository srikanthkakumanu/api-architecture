import { Activity, CalendarClock, UserRoundPlus, UsersRound } from "lucide-react";
import { ClinicDashboard } from "@/components/ClinicDashboard";
import { getAppointments, getPatients } from "@/lib/api";

export default async function Page() {
  const [patients, appointments] = await Promise.all([getPatients(), getAppointments()]);

  return (
    <main className="shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Clinic Desk</p>
          <h1>Reception Workspace</h1>
        </div>
        <div className="status-pill">
          <Activity aria-hidden="true" size={18} />
          Prototype
        </div>
      </header>

      <section className="metrics" aria-label="Clinic snapshot">
        <div className="metric">
          <UsersRound aria-hidden="true" size={20} />
          <span>{patients.length}</span>
          <p>Patients</p>
        </div>
        <div className="metric">
          <CalendarClock aria-hidden="true" size={20} />
          <span>{appointments.length}</span>
          <p>Appointments</p>
        </div>
        <div className="metric">
          <UserRoundPlus aria-hidden="true" size={20} />
          <span>{appointments.filter((appointment) => appointment.status === "SCHEDULED").length}</span>
          <p>Scheduled</p>
        </div>
      </section>

      <ClinicDashboard initialPatients={patients} initialAppointments={appointments} />
    </main>
  );
}
