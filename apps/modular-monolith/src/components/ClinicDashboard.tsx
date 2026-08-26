"use client";

import { CalendarPlus, ClipboardList, RefreshCw, UserRoundPlus, UsersRound } from "lucide-react";
import { FormEvent, useMemo, useState, useTransition } from "react";
import {
  Appointment,
  Patient,
  createAppointment,
  createPatient,
  getAppointments,
  getPatients,
} from "@/lib/api";

type Props = {
  initialPatients: Patient[];
  initialAppointments: Appointment[];
};

type ActiveForm = "patient" | "appointment";

export function ClinicDashboard({ initialPatients, initialAppointments }: Props) {
  const [activeForm, setActiveForm] = useState<ActiveForm>("patient");
  const [patients, setPatients] = useState(initialPatients);
  const [appointments, setAppointments] = useState(initialAppointments);
  const [message, setMessage] = useState<string | null>(null);
  const [isPending, startTransition] = useTransition();

  const nextPatientId = useMemo(() => patients[0]?.id ?? 1, [patients]);

  function refresh() {
    startTransition(async () => {
      setMessage(null);
      const [latestPatients, latestAppointments] = await Promise.all([getPatients(), getAppointments()]);
      setPatients(latestPatients);
      setAppointments(latestAppointments);
    });
  }

  function submitPatient(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    startTransition(async () => {
      setMessage(null);
      try {
        const patient = await createPatient({
          fullName: String(form.get("fullName")),
          email: String(form.get("email")),
          phoneNumber: String(form.get("phoneNumber")),
          dateOfBirth: String(form.get("dateOfBirth")),
        });
        setPatients((current) => [patient, ...current]);
        event.currentTarget.reset();
      } catch (error) {
        setMessage(error instanceof Error ? error.message : "Unable to register patient.");
      }
    });
  }

  function submitAppointment(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    startTransition(async () => {
      setMessage(null);
      try {
        const appointment = await createAppointment({
          patientId: Number(form.get("patientId")),
          doctorName: String(form.get("doctorName")),
          startsAt: String(form.get("startsAt")),
          reason: String(form.get("reason")),
        });
        setAppointments((current) => [...current, appointment].sort((a, b) => a.startsAt.localeCompare(b.startsAt)));
        event.currentTarget.reset();
      } catch (error) {
        setMessage(error instanceof Error ? error.message : "Unable to schedule appointment.");
      }
    });
  }

  return (
    <section className="workspace">
      <aside className="panel">
        <div className="panel-header">
          <h2>{activeForm === "patient" ? "Register Patient" : "Schedule Appointment"}</h2>
          <div className="tabs" aria-label="Form selection">
            <button
              className={`tab ${activeForm === "patient" ? "active" : ""}`}
              type="button"
              onClick={() => setActiveForm("patient")}
              title="Register patient"
            >
              <UserRoundPlus aria-hidden="true" size={18} />
            </button>
            <button
              className={`tab ${activeForm === "appointment" ? "active" : ""}`}
              type="button"
              onClick={() => setActiveForm("appointment")}
              title="Schedule appointment"
            >
              <CalendarPlus aria-hidden="true" size={18} />
            </button>
          </div>
        </div>

        {activeForm === "patient" ? (
          <form className="form" onSubmit={submitPatient}>
            <Field name="fullName" label="Full name" placeholder="Maya Patel" required />
            <Field name="email" label="Email" placeholder="maya@example.com" required type="email" />
            <Field name="phoneNumber" label="Phone" placeholder="+1-555-0100" required />
            <Field name="dateOfBirth" label="Date of birth" required type="date" />
            {message ? <p className="notice">{message}</p> : null}
            <button className="command" disabled={isPending} type="submit">
              <UserRoundPlus aria-hidden="true" size={18} />
              Register
            </button>
          </form>
        ) : (
          <form className="form" onSubmit={submitAppointment}>
            <Field
              name="patientId"
              label="Patient ID"
              min="1"
              placeholder={String(nextPatientId)}
              required
              type="number"
            />
            <Field name="doctorName" label="Doctor" placeholder="Dr. Lee" required />
            <Field name="startsAt" label="Starts at" required type="datetime-local" />
            <Field name="reason" label="Reason" placeholder="Annual checkup" required />
            {message ? <p className="notice">{message}</p> : null}
            <button className="command" disabled={isPending} type="submit">
              <CalendarPlus aria-hidden="true" size={18} />
              Schedule
            </button>
          </form>
        )}
      </aside>

      <div className="lists">
        <DataPanel
          icon={<UsersRound aria-hidden="true" size={18} />}
          title="Patients"
          action={
            <button className="tab" disabled={isPending} onClick={refresh} title="Refresh" type="button">
              <RefreshCw aria-hidden="true" size={17} />
            </button>
          }
        >
          {patients.length === 0 ? (
            <div className="empty">No patients registered.</div>
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map((patient) => (
                    <tr key={patient.id}>
                      <td>{patient.id}</td>
                      <td>{patient.fullName}</td>
                      <td>{patient.email}</td>
                      <td>{patient.phoneNumber}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </DataPanel>

        <DataPanel icon={<ClipboardList aria-hidden="true" size={18} />} title="Appointments">
          {appointments.length === 0 ? (
            <div className="empty">No appointments scheduled.</div>
          ) : (
            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Time</th>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Reason</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments.map((appointment) => (
                    <tr key={appointment.id}>
                      <td>{formatDateTime(appointment.startsAt)}</td>
                      <td>{appointment.patientName}</td>
                      <td>{appointment.doctorName}</td>
                      <td>{appointment.reason}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </DataPanel>
      </div>
    </section>
  );
}

function Field(props: {
  name: string;
  label: string;
  placeholder?: string;
  required?: boolean;
  type?: string;
  min?: string;
}) {
  return (
    <div className="field">
      <label htmlFor={props.name}>{props.label}</label>
      <input id={props.name} {...props} />
    </div>
  );
}

function DataPanel({
  action,
  children,
  icon,
  title,
}: {
  action?: React.ReactNode;
  children: React.ReactNode;
  icon: React.ReactNode;
  title: string;
}) {
  return (
    <section className="panel">
      <div className="panel-header">
        <h2>
          {icon} {title}
        </h2>
        {action}
      </div>
      {children}
    </section>
  );
}

function formatDateTime(value: string) {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}
